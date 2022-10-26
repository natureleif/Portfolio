#include "Object.h"

class Sphere: public Object{
    public:

    Sphere();

    Sphere(Tuple Origin, double Radius, Rgb ambient, Rgb diffuse, Rgb specular, int specExp);

    double r;
    
    bool intersect(const Ray& ray, double& distance);
};