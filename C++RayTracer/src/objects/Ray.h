#include "Tuple.h"
#pragma once

class Ray{
    public:

    Tuple direction;
    Tuple origin;

    // default constructor
    Ray();

    // generate a ray between two points
    Ray(Tuple origin, Tuple end); 

    // set the values of ray between two points
    void set(Tuple origin, Tuple end);

};