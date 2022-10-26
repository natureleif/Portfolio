#include "Scene.h"

int main() {

    Scene image(3000,4,4,4);
    image.createSphere(Tuple(0,0,12,1),2,Rgb(.5,.5,.5),Rgb(.8,.8,.8),Rgb(1,1,1),5); //white
    image.createSphere(Tuple(0,3,10,1),2,Rgb(.5,.5,.5),Rgb(.8,.8,.8),Rgb(1,1,1),5); //white
    image.createSphere(Tuple(-2,-3,10,1),2,Rgb(.5,.5,.5),Rgb(.8,.8,.8),Rgb(1,1,1),5); //white
    image.createSphere(Tuple(2,-1,8,1),2,Rgb(.5,.5,.5),Rgb(.8,.8,.8),Rgb(1,1,1),5); //white

    
    image.createLight(new Light(Rgb(.1,0,0),Rgb(.2,0,0),Rgb(.4,0,0),Tuple(5,5,5,1))); //red light
    image.createLight(new Light(Rgb(0,.1,0),Rgb(0,.2,0),Rgb(0,.4,0),Tuple(0,-5,5,1))); //green light
    image.createLight(new Light(Rgb(0,0,.1),Rgb(0,0,.2),Rgb(0,0,.4),Tuple(-5,0,5,1))); //blue light
    image.createLight(new Light(Rgb(0,0,0),Rgb(.2,.2,.2),Rgb(.4,.4,.4),Tuple(-2,0,14,1))); //white light

    image.createPlane(Tuple(0,0,14,1),Tuple(0,0,1),Rgb(0,0,0),Rgb(.1,.1,.1),Rgb(.5,.5,.5),1);

    image.render("test.ppm");

    return 0;
}
