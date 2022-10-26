#include "Plane.h"

Plane::Plane(): Object {} {
    this->origin = Tuple(0,0,0,1);
    this->normal = Tuple(0,0,1,1);
    this->objType = 0;

}

Plane::Plane(Tuple Origin, Tuple Normal, Rgb ambient, Rgb diffuse, Rgb specular, int specExp): Object {} {
    Normal.normalize();
    this->origin = Origin;
    this->normal = Normal;
    this->ambientMaterial = ambient;
    this->diffuseMaterial = diffuse;
    this->specularMaterial = specular;
    this->specularExponent = specExp;
    this->objType = 0;
}

bool Plane::intersect(const Ray& ray, double& distance){
    // plane ray intersect
    float den = this->normal.dot(ray.direction);
    if (abs(den) > 0.001f){
        distance = (this->origin - ray.origin).dot(this->normal) / den;
        if (distance > 0) return true;
    }
    return false;
}

Tuple Plane::getNormal(){
    return this->normal;
}
