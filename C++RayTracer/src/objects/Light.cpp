#include "Light.h"

Light::Light(){
    this->ambient = Rgb(1,1,1);
    this->diffuse = Rgb(1,1,1);
    this->specular = Rgb(1,1,1);
    this->lightPoint = Tuple(0,0,0,1);
}

Light::Light(double intensity, Tuple position){
    if(intensity > 1){intensity = 1;}
    if(intensity < 0){intensity = 0;}
    
    this->ambient = Rgb(intensity,intensity,intensity);
    this->diffuse = Rgb(intensity,intensity,intensity);
    this->specular = Rgb(intensity,intensity,intensity);
    this->lightPoint = position;
}

Light::Light(Rgb ambientIntensity, Rgb diffuseIntensity, Rgb specularIntensity, Tuple position){
    this->ambient = ambientIntensity;
    this->diffuse = diffuseIntensity;
    this->specular = specularIntensity;
    this->lightPoint = position;
}