/*
    Copyright (C) 2007  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package fractals;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputListener;

final class CanvasViewInputHandler implements MouseInputListener, MouseWheelListener, KeyListener
{

    CanvasView canvasView;
    private Point previousPoint;

    public CanvasViewInputHandler(CanvasView canvasView)
    {
        super();
        this.canvasView = canvasView;
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseDragged(MouseEvent e)
    {
        //System.out.println(e);
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            updateDrag(e.getPoint(), e.isShiftDown());
        }
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseMoved(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        //System.out.println(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            previousPoint = e.getPoint();
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        //System.out.println(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            updateDrag(e.getPoint(), e.isShiftDown());
            previousPoint = null;
        }
    }

    private void updateDrag(Point currentPoint, boolean isRotate)
    {
        int dispX = currentPoint.x - previousPoint.x;
        int dispY = currentPoint.y - previousPoint.y;
        if (isRotate) {
            canvasView.rotateBy(dispY * 0.01);
        } else {
            canvasView.moveBy(dispX, dispY);
        }
        previousPoint = currentPoint;
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        switch (Integer.signum(e.getWheelRotation())) {
            case -1:
                // Up rotation
                canvasView.zoomBy(1);
                break;
            case 1:
                // Down rotation
                canvasView.zoomBy(-1);
                break;
            default:
                throw new IllegalArgumentException("getWheelRotation() reported zero");
        }
    }

    public void keyPressed(KeyEvent e)
    {
    }

    public void keyReleased(KeyEvent e)
    {
        //System.out.println(e);
        switch (e.getKeyChar()) {
            case '+':
                canvasView.zoomBy(1);
                break;
            case '-':
                canvasView.zoomBy(-1);
                break;
            default:
        }
    }

    public void keyTyped(KeyEvent e)
    {
    }
}
