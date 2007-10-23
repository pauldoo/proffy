// DrawScreen.cpp : Main work house of the code. Entry Point.
//
#include "stdafx.h"
#include "GlobalMFC.h"
#include "GlobalWindowSize.h"
#include "DrawScreen.h"
#include "World.h"
#include "Plane.h"
#include "Sphere.h"
#include "Solid.h"
#include "Test.h"
#include "ProjectionPlane.h"
#include "CheckerMap.h"
#include "ApplyTexture.h"
#include "ColorMap.h"
#include "TransformationAdapter.h"
#include "Torus.h"
#include "Juliaset.h"
#include "Line.h"
#include "Intersect.h"
#include "Maybe.h"
#include "TwirlyBump.h"
#include "BumpMapAdapter.h"
#include "SolidUnion.h"
#include "Fold.h"
#include "RenderInfo.h"
#include "Light.h"
#include "StandardRenderMask.h"
#include "Timer.h"
#include "TimingPool.h"
#include "ColorFuncs.h"
#include <fstream>


namespace {
	/*	This Function Takes the CoOrdinates of the pixel and renders it. It does this by First Projecting a 
	line through the pixel and seeing where it intersects with the solids in the universe and then rendering 
	that particular point. The handle must also be passed to give it the ability to draw pixels to it */

	// standard windows procedure
	bool CheckForMessages()
	{
		MSG msg;
		if (PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)) { 
			// test if this is a quit
			if (msg.message == WM_QUIT) {
				PostQuitMessage(0);
				return true;
			}
	
			// translate any accelerator keys
			TranslateMessage(&msg);

			// send the message to the window proc
			DispatchMessage(&msg);
		}
		return false;
	}

	void SetAPixel(const int i, const int j, const Color& color) {
		SetPixelV(hdc, i, j, ColorFuncs::ToCOLORREF(color));
	}

	void RenderPixel(const int x, const int y, const Auto<const World>& world, RenderMemory& renderMemory) {
		
		const Line lightRay = world->GetProjectionPlane().ProjectFromPixel(x,y);
		
		Intersect00 closestIntersect;
		{
			TIMETHISBLOCK("Initial Ray Casting");
			closestIntersect = 
				world->GetSolid()->DetermineClosestIntersectionPoint(
					lightRay,
					eRender,
					renderMemory);
		}
		
		if (closestIntersect.IsValid()) {
			TIMETHISBLOCK("Actual Rendering");
			const Color color = closestIntersect.Get()->Render(
				new RenderInfo(closestIntersect.Get(), world, lightRay), renderMemory);
			SetAPixel(x, y, color);
		} else {
			SetAPixel(x, y, Color(1,1,4));
		}
	}
}

void DrawScreen::DoIt() {
	// First Create the World.

	Test();
	TimingPool::ClearAllTimers();

	const Auto<const Solid> shapes =
		Fold(MakeSolidUnion,
			MakeTransformationAdapter(
				MakeApplyTexture(
					MakePlane(),
					MakeStandardRenderMask(new CheckerMap())
				),
				Matrix(Point(1,0,0),Point(0,0,1),Point(0,1,0)),
				Point(0,-1,0)
			),
			MakeTransformationAdapter(
				MakeApplyTexture(
					MakeSphere(.25),
					MakeStandardRenderMask(new ColorMap(Color(2,0,0)))
				),
				IdentityMatrix(),
				Point(-1,0,4)
			),
			MakeTransformationAdapter(
				MakeApplyTexture(
					MakeSphere(.25),
					MakeStandardRenderMask(new ColorMap(Color(0,2,0)))
				),
				IdentityMatrix(),
				Point(1,0,4)
			),
			MakeTransformationAdapter(
				MakeBumpMapAdapter(
					MakeApplyTexture(
						MakeTorus(.8, 1),
						MakeStandardRenderMask(new JuliaSet())
					),
					new TwirlyBump()
				),
				AlignCoordsSys(Point(-1,1,-1)),
				Point(0,1,6)
			)
		);

	World::LightList lights;
	lights.push_back(new Light(Point(-4,1,4), Color(1,.1,.1) * .6));
	lights.push_back(new Light(Point(2,1,1), Color(.1,1,.1) * .6));
	lights.push_back(new Light(Point(-2,7,1), Color(1,1,1) * .4));

	const Auto<const World> world = new World(
		shapes, 
		ProjectionPlane(), 
		lights);

	// The height and width of the drawing area (window size minus stuff on the side).
    int width  = drawing_pane_width; 
    int height = drawing_pane_height; 
	

	// The x and y denote the coordinates of the pixel we are drawing (starting position = top left corner).
	int x = 0, y = 0;	

	// Instead of a regular left-right pan I chose a loop which moves it's way around the 
	// drawing area in a square spiral.
	// This loop has the advantage that consecutive pixels are adjacent 
	// which means that we can use the t for one as a initial guess for the next.
	// NOTE: Code must take account of the fact that the screen may not be square
	// (causes problems at the end).
	hdc = GetDC(hWnd);	
	const Timer time = Timer().Start();
	{ TIMETHISBLOCK("Total Timing");
	do																
	{	
		RenderMemory renderMemory;
		if(width) {
			for (int dx = 0 ; dx < width ; dx++, x++) {				// Move left across the screen
				RenderPixel(x,y,world,renderMemory);
			}
			if (CheckForMessages()) return;
			width--;
		}
		if(height) {
			for (int dy = 0 ; dy < height  ; dy++, y++) {			// Move down across the screen
				RenderPixel(x,y,world,renderMemory);	
			}
			if (CheckForMessages()) return;
			height--;
		}
		if(width) {
			for (int dx = 0 ; dx < width ; dx++, x--) {				// Move right across the screen
				RenderPixel(x,y,world,renderMemory);		
			}
			if (CheckForMessages()) return;
			width--;
		}
		if(height) {
			for (int dy = 0 ; dy < height ; dy++, y--) {			// Move up across the screen
				RenderPixel(x,y,world,renderMemory);	
			}
			if (CheckForMessages()) return;
			height--;
		}
	} while(width || height); }
	PersistentConsole::OutputString("Total time was " + ToString(time.CurrentTime()) + "\n");
	std::ofstream out("timing.txt");
	out << TimingPool::TimingSummary();
	ReleaseDC(hWnd,hdc);
}