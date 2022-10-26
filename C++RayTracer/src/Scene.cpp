#include "Scene.h"

//Default constructor
Scene::Scene(){
    this->size = 100;
    this->w = 1;
    this->h = 1;
    this->front = 1;
    this->numObjects = 0;
    this->numLights = 0;
}

Scene::Scene(int imagePixelSize, double width, double height, double front_clip){
    this->size = imagePixelSize;
    this->w = width;
    this->h = height;
    this->front = front_clip;
    this->numObjects = 0;
    this->numLights = 0;
}


// create a sphere in our workplace and return true when done
bool Scene::createSphere(Tuple origin, double radius, Rgb ambient, Rgb diffuse, Rgb specular, int specExp){
    this->objects[this->numObjects] = new Sphere(origin, radius, ambient, diffuse, specular, specExp);
    this->numObjects++;
    return true;
}

// create a plane in the workspace
bool Scene::createPlane(Tuple origin, Tuple normal, Rgb ambient, Rgb diffuse, Rgb specular, int specExp){
    this->objects[this->numObjects] = new Plane(origin, normal, ambient, diffuse, specular, specExp);
    this->numObjects++;
    return true;
}


//creates or sets the light point
bool Scene::createLight(Light* light){
    this->lights[this->numLights] = light;
    this->numLights++;
    return true;
}

//check all object intersects
bool Scene::_intersectObjects(const Ray& ray, double& closest, int& closestObj){
    closest = 5000; // This is the back clip
    closestObj = -1;
    double distance = 0;
    for(int p = 0; p < numObjects; p++){
                if(objects[p]->intersect(ray,distance)){
                    if(distance < closest){
                        closest = distance;
                        closestObj = p;
                    }
                }
            }
    if(closestObj > -1){return true;}
    return false;
}


//Render the image and output with filename
void Scene::render(std::string filename){
    PPM myRender = easyppm_create(this->size, this->size, IMAGETYPE_PPM);

    // Clear all image pixels to RGB color black.
    easyppm_clear(&myRender, easyppm_rgb(0, 0, 0));

    Ray R;
    Ray L;
    Tuple B(-(this->w/2),0 -(this->h/2), this->front, 1); // bottom left corner of screen
    Tuple Camera(0,0,0,1);
    Tuple X(1,0,0,0);
    Tuple Y(0,1,0,0);
    Tuple Z(0,0,1,0);

    for(int i = 0; i < this->size; i++){
        for(int j = 0; j < this->size; j++){

            float stepX = i/(this->size/this->w);
            float stepY = j/(this->size/this->h);
            Tuple P = B + stepX*X + stepY*Y;

            //Set normal vector into scene
            R.set(Camera, P);
            
            //Ray object intersects
            double closest;
            int closestObj;
            bool hit = _intersectObjects(R, closest, closestObj);
            
            //Light calculations
            if(hit){ //if an object is it
                Rgb pixel;
                Tuple objectPoint = (R.direction * closest) + Camera; //point of intersect
                Rgb greatestAmbient;
                
                for(int l = 0; l < numLights; l++){ //loops throught all lights
                    L.set(objectPoint, lights[l]->lightPoint); //Creates a ray from the intersect to the current light
                    if(lights[l]->ambient.getR() > greatestAmbient.getR()){greatestAmbient.setR(lights[l]->ambient.getR());} //next three lines find biggest ambient values among all lights
                    if(lights[l]->ambient.getG() > greatestAmbient.getG()){greatestAmbient.setG(lights[l]->ambient.getG());} 
                    if(lights[l]->ambient.getB() > greatestAmbient.getB()){greatestAmbient.setB(lights[l]->ambient.getB());} 

                    double temp;
                    bool shadow = false;
                    for(int p = 0; p < numObjects; p++){ //checks for intersects along ray L
                        if(objects[p]->intersect(L, temp) && p != closestObj && temp > 0 && temp < L.direction.magnitude()){
                            shadow = true;
                        }
                    }
                    if(!shadow){
                        Tuple objectNormal;
                        Rgb diffuseIntensity = lights[l]->diffuse;
                        Rgb specularIntensity = lights[l]->specular;

                        if(objects[closestObj]->objType == 0){ //if object is a plane
                            objectNormal = objects[closestObj]->getNormal();
                        }

                        else{ //if object is a sphere
                            objectNormal = objectPoint - objects[closestObj]->origin;
                        }

                        objectNormal.normalize();

                        Rgb diffuse = lightDiffuse(objects[closestObj]->diffuseMaterial, objectPoint, objectNormal, diffuseIntensity, lights[l]->lightPoint);
                        Rgb specular = lightSpecular(objects[closestObj]->specularMaterial, objectPoint, objectNormal, specularIntensity, lights[l]->lightPoint, Camera, objects[closestObj]->specularExponent);
                        pixel = pixel + diffuse + specular;
                    }
                }
                // add ambient light at the end
                pixel = pixel + lightAmbient(objects[closestObj]->ambientMaterial, greatestAmbient);
                easyppm_set(&myRender, i, this->size -1 - j, easyppm_rgb(pixel.getR()*255, pixel.getG()*255, pixel.getB()*255));
            }
        }
    }
    easyppm_write(&myRender, ("out/"+filename).c_str());
    easyppm_destroy(&myRender);
}

