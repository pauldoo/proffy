#include <iostream>
#include <cmath>
#include <cstdlib>

using namespace std;

#define SCALE 1;

const int width = 1600 * SCALE;// * 8;
const int height = 1200 * SCALE;// * 8;

const int layers = 20000 * SCALE;// * 8;
const int iters = 2000 * SCALE;// * 8;

const int steps = 250;

double dist = 15;
double xscale = 1;
double yscale = 1;
double zscale = 1;

unsigned char image[height][width] = { { 0 } };

void plot(int x, int y) {
  if ((x >= 0) && (x < width) && (y >= 0) && (y < height) && (image[y][x] < 255))
    image[y][x]++;
}

void plot(double x, double y, double z) {
  x *= xscale;
  y *= yscale;
  z *= zscale;
  z += dist;
  if (z <= 0)
    return;
  int xi = static_cast<int>( (x / z) * height + (width/2) );
  int yi = static_cast<int>( (y / z) * height + (height/2) );
  plot (xi, yi);
}

int sign(double c) {
  return (c < 0) ? (-1) : (1);
}

int main(int argc, char** argv) {
  int frame = 0;
  if (argc >= 2) {
    frame = atoi(argv[1]); 
    cerr << "Rendering frame #" << frame << "\n";
  }

  double angle = (M_PI * 2 * frame) / steps, ca = cos(angle), sa = sin(angle);

  cerr << "Angle: " << angle << "\n";

  double a = 0, b = 1, c = 2;
  int percent = -1;

  for (int layer = 0; layer < layers; layer++) {
    int p = ((layer+1)*100)/layers;
    if (p != percent) {
      percent = p;
      cerr << "\r" << percent << "%";
    }

    a = (layer * 5.0) / layers;
    //cerr << "a: " << a << "\tb: " << b << "\tc: " << c << "\n";
    double x = 2, y = 3, z = (a/5.0) * 20 - 10;
    for (int i = 0; i < iters; i++) {
      
      {
	double t = x;
	x = y - sign(x) * sqrt(fabs(x * b + c));
	y = a - t;
      }     
      //cerr << "x: " << x << "\ty: " << y << "\tz: " << z << "\n";    
      //plot (z, y, x);
      plot (ca * z + sa * x,
	    y,
	    -sa * z + ca * x);
	    
	    
    }
  }
  
  cerr << "\n";

  cout << "P5\n" << width << " " << height << "\n255\n";
  cout.write((const char*)image, height * width);
  return 0;
}
