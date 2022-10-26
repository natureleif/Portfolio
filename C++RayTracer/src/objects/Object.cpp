#include "Object.h"

Object::Object(){
    this->t = 0;
}

bool Object::intersect(const Ray& ray, double& distance){
    return false;
}

Tuple Object::getNormal(){
    return Tuple();
}