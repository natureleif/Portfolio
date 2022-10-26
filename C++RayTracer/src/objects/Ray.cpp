#include "Ray.h"

Ray::Ray(){
    this->direction = Tuple(0,0,0,0);
    this->origin = Tuple(0,0,0,1);
}

// generate a ray between two points
Ray::Ray(Tuple origin, Tuple end){
    this->origin = origin;
    this->direction = end - origin;
    this->direction.normalize();
}

// set the values of ray between two points
void Ray::set(Tuple origin, Tuple end){
    this->origin = origin;
    this->direction = end - origin;
    this->direction.normalize();
}
