#pragma once
#include "Tuple.h"
#include "Ray.h"
#include "Rgb.h"

class Object{
    public:

    Object();

    Rgb color;
    Rgb ambientMaterial;
    Rgb diffuseMaterial;
    Rgb specularMaterial;	
	int specularExponent;

    double t;

    Tuple origin;
    
    // 0 for plane 1 for sphere -1 for undefined
    int objType;

    virtual bool intersect(const Ray& ray, double& distance);

    virtual Tuple getNormal();
};