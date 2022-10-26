#include "Object.h"

class Plane: public Object {
    public:

    Plane();

    Plane(Tuple Origin, Tuple Normal, Rgb ambient, Rgb diffuse, Rgb specular, int specExp);

    
    Tuple normal;

    bool intersect(const Ray& ray, double& distance);

    Tuple getNormal();

};