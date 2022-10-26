#include "Sphere.h"

Sphere::Sphere(): Object {} {
    this->origin = Tuple(0,0,0,1);
    this->r = 1;
    this->objType = 1;
}

Sphere::Sphere(Tuple Origin, double Radius, Rgb ambient, Rgb diffuse, Rgb specular, int specExp): Object {} {
    this->origin = Origin;
    this->r = Radius;
    this->ambientMaterial = ambient;
    this->diffuseMaterial = diffuse;
    this->specularMaterial = specular;
    this->specularExponent = specExp;
    this->objType = 1;
}

bool Sphere::intersect(const Ray& ray, double& distance){
    // sphere ray intersect
    double a = ray.direction.dot(ray.direction);
    Tuple V1 = ray.origin - this->origin;
    

    //std::cout<<"R: "<<ray<<std::endl;
    double b = 2 * V1.dot(ray.direction);
    double c = V1.dot(V1) - (this->r * this->r);

    double discriminant = b*b - 4*a*c; //note: discriminants is always 25 and abc always the same...
    
    if (discriminant > 0) {
        double x1 = (-b + sqrt(discriminant)) / (2*a);
        double x2 = (-b - sqrt(discriminant)) / (2*a);
        distance = min(x1,x2);
        return true;
    }

    else if (discriminant == 0) {
        distance = -b/(2*a);
        return true;
    }
    else {
        distance = -1;
        return false;
    }
}
