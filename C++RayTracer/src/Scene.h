#include <stdio.h>
#include <math.h>
#include <cmath>
#include <vector>
#include "objects/Ray.h"
#include "objects/Plane.h"
#include "objects/Sphere.h"
#include "objects/Lighting.h"
#include "objects/Light.h"
#include "objects/easyppm.h"


class Scene{

    public:
        //Default constructor
        Scene();

        Scene(int imagePixelSize, double width, double height, double front_clip);

        // create a sphere in our workplace and return true when done
        bool createSphere(Tuple position, double radius, Rgb ambient, Rgb diffuse, Rgb specular, int specExp);

        // create a plane in the workspace
        bool createPlane(Tuple position, Tuple vector, Rgb ambient, Rgb diffuse, Rgb specular, int specExp);

        //check all object intersects
        bool _intersectObjects(const Ray& ray, double& closest, int& closestObj);

        //creates or sets the light point
        bool createLight(Light* light);

        //Render the image and output with filename
        void render(std::string filename);

    private:

        int size;
        double h;
        double w;
        double front;

        Object* objects[10];
        Light* lights[5];
        //vector<Object*> objects;
        //vector<Light*> lights;
        
        int numObjects;
        int numLights;
};