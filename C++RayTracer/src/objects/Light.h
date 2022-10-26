#include "Tuple.h"
#include "Rgb.h"

class Light{
    public:

    Light();
    Light(double intensity, Tuple position);
    Light(Rgb ambientIntensity, Rgb diffuseIntensity, Rgb specularIntensity, Tuple position);

    Rgb ambient;
    Rgb diffuse;
    Rgb specular;
    Tuple lightPoint;

};