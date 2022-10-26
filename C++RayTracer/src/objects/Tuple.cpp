#include "Tuple.h"
#include <cstdlib>


// Default constructor - initializes Tuple to be a vector having coordinate
// values (x 0, y 0, z 0, w 0)
Tuple::Tuple(){
    this->x = 0;
    this->y = 0;
    this->z = 0;
    this->w = 0;
}

// Vector constructor - initializes Tuple to be a vector having coordinate
// values x, y, and z as given. The w value will be automatically set to 0.
Tuple::Tuple(double x, double y, double z){
    this->x = x;
    this->y = y;
    this->z = z;
    this->w = 0;
}

// Point constructor - initializes Tuple to be a point having coordinate
// values x, y, and z as given. The fourth argument is expected to be any
// positive value, but will be always stored as w = 1.0 
// The C++ compiler will call this version when it sees four type double arguments.
Tuple::Tuple(double x, double y, double z, double w){
    this->x = x;
    this->y = y;
    this->z = z;
    this->w = w;
}

// Copy constructor - receives another Tuple as argument.
// Initialize this Tuple to hold identical x, y, z, and w values as the given argument Tuple.
Tuple::Tuple(const Tuple& source){
    this->x = source.x;
    this->y = source.y;
    this->z = source.z;
    this->w = source.getW();
}

// Return true if this Tuple is a vector (has w value of zero)
// Hint: Use the absolute value function check for zero.
// If the absolute value of w is less than 0.001 (or similar small epsilon value), then we 
// assume that the w value is "equal" to zero.
// When using float or double the == can often report false when the two
// values are close enough to being equal.
// https://cplusplus.com/reference/cmath/abs/
bool Tuple::isVector() const{
    if (abs(this->w) < 0.001){
        return true;
    }
    return false;
}

// Return true if this Tuple is a point (has w value > 0)
bool Tuple::isPoint() const{
    if (abs(this->w) > 0.001){
        return true;
    }
    return false;
}

// Increments this x, y, z, and w by respective values from otherTuple.
// If this tuple and otherTuple are both points whose w values are 1, then
// the addition will leave this->w at 2.0. If we find this->w > 1, then set this->w = 1.
void Tuple::add(const Tuple& otherTuple){
    this->x += otherTuple.x;
    this->y += otherTuple.y;
    this->z += otherTuple.z;
    this->w += otherTuple.getW();
    if(this->w > 1){
        this->w = 1.0;
    }
}

// Decrements this x, y, z, and w by respective values from otherTuple.
// If this tuple is a vector and otherTuple a point whose w values are 1, then
// the subtraction will leave this->w at -1. If we find this->w < -0.5, then set this->w = 1
// so that the result is a point.
void Tuple::sub(const Tuple& otherTuple){
    this->x -= otherTuple.x;
    this->y -= otherTuple.y;
    this->z -= otherTuple.z;
    this->w -= otherTuple.getW();
    if(this->w < -0.5){
        this->w = 1.0;
    }
}

// Multiplies x, y, and z values of this Tuple by the given scalar value S.
// Tuple's w value is not changed.
void Tuple::multScalar(double S){
    this->x = this->x * S;
    this->y = this->y * S;
    this->z = this->z * S;
}

// Return the length or magnitude of this point or vector.
// If Tuple is a point, then length is the distance of this point from the origin point (0,0,0).
// If Tuple is a vector, then length is the magnitude of the vector.
// The Tuple's w value does not contribute to its length.
double Tuple::magnitude() const{
    return sqrt(pow(this->x,2.0) + pow(this->y,2.0) + pow(this->z,2.0));
}

// Change this vector to unit-length. 
// A unit-length vector is one whose length or magnitude is 1.0.
// pre-condition: This Tuple must be a vector. If is a point, then do nothing.
// First, find the magnitude of this vector.
// Second, if the magnitude is > 0, then divide this Tuple's
// x, y, and z values by the magnitude.
// Do not divide when the magnitude is 0 since this will cause a divide by zero crash.
// Normalize does not change the Tuple's w value.
void Tuple::normalize(){
    if (abs(this->w) < 0.001){
        double mag = magnitude();
        if(mag > 0){
            this->x = this->x / mag;
            this->y = this->y / mag;
            this->z = this->z / mag;
        }
    }
}

// Return the dot product of two vectors.
// pre-condition: this Tuple and otherTuple must both be vectors.
// returns dot product of this Tuple and otherTuple using only their x, y, and z values.
// Otherwise, return 0 if this Tuple and otherTuple are not both vectors.
double Tuple::dot(const Tuple& otherTuple) const{
    if(abs(this->w) > 0.001 || abs(otherTuple.getW()) > 0){
        return 0;
    }
    return(this->x * otherTuple.x) + (this->y * otherTuple.y) + (this->z * otherTuple.z);
}

// Print formatted display of Tuple to std::cout.
// Example print out: (1, 2, 3, 0) for a vector.
// Print the values in order of x, y, z, and w.
void Tuple::print() const{
    cout <<"("<<this->x<<", "<<this->y<<", "<<this->z<<", "<<this->w<<")"<<endl;
}

// Returns the value of w
double Tuple::getW() const{
    return this->w;
}

// Overloaded operators. The class declares these functions as its 'friends.'
// The implementation code for the friend functions will receive Tuple object arguments.
// The friend functions will be able to access the private data attributes
// by saying name_of_tuple_argument.x, etc.
// We will not prefix friend implemenations with Tuple:: since they are not
// class member functions.

// Reference: https://docs.microsoft.com/en-us/cpp/standard-library/overloading-the-output-operator-for-your-own-classes?view=msvc-170
ostream& operator<<(ostream& os, const Tuple& T){
    os <<"("<<T.x<<", "<<T.y<<", "<<T.z<<", "<<T.getW()<<")";
	return os; // send out the ostream
}
        
//--- Allows us to write A + B to compute the Tuple that results from calling A.add(B)
//--- Code-reuse tip - Call on your add method
Tuple operator+(const Tuple& a, const Tuple& b){
    Tuple newTup = Tuple(a); 
    newTup.add(b);
    return newTup;
}
    
//--- Allows us to write A - B to compute the Tuple that results from calling A.sub(B)
//--- Code-reuse tip - Call on your sub method    
Tuple operator-(const Tuple& a, const Tuple& b){
    Tuple newTup = Tuple(a); 
    newTup.sub(b);
    return newTup;
}
        
//--- Allows us to write A * 1.5 to compute the Tuple that results from calling A.multScalar(1.5)
//--- Code-reuse tip - Call on your multScalar method
Tuple operator*(const Tuple& a, double s){
    Tuple newTup = Tuple(a);
    newTup.multScalar(s);
    return newTup;
}
    
//--- Allows us to write 1.5 * A to compute the Tuple that results from calling A.multScalar(1.5)
//--- Code-reuse tip - Call on your multScalar method    
Tuple operator*(double s, const Tuple& a){
    Tuple newTup = Tuple(a);
    newTup.multScalar(s);
    return newTup;
}

