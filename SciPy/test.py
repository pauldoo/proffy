#!/usr/bin/env python
# Playing with nearest neighbour, linear, and cubic interpolation of a volume.


from matplotlib.pyplot import imshow
from numpy import cos
from scipy import ogrid, sin, mgrid, ndimage, array
from scipy.misc import imsave

x,y,z = ogrid[-1:1:16j,-1:1:16j,-1:1:16j]
fvals = cos(20 * (x**2 + y**2 + z**2)**0.5)
#for index in range(0, fvals.shape[2]):
#    imsave('/tmp/input_%04i.png' % index, fvals[:, :, index])

newx,newy,newz = mgrid[-1:1:200j,-1:1:200j,-1:1:200j]
x0 = x[0,0,0]
y0 = y[0,0,0]
z0 = z[0,0,0]
dx = x[1,0,0] - x0
dy = y[0,1,0] - y0
dz = z[0,0,1] - z0
ivals = (newx - x0)/dx
jvals = (newy - y0)/dy
kvals = (newz - z0)/dz
coords = array([ivals, jvals, kvals])
for o in [0, 1, 3]:
    #newf = ndimage.filters.gaussian_gradient_magnitude(ndimage.map_coordinates(fvals, coords, order=o), 0.5)
    newf = ndimage.map_coordinates(fvals, coords, order=o)
    for index in range(0, newf.shape[2]):
        imsave('/tmp/interp/output_%i_%04i.png' % (o, index), newf[:, :, index])

