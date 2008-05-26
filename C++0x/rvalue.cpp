#include <iostream>

namespace {
    class MyClass
    {
    public:
	MyClass()
	{
	}

    public:
	MyClass(const MyClass& v)
	{
	    std::cout << " * Reached normal copy constructor.\n";
	}

    public:
	MyClass(MyClass&& v)
	{
	    std::cout << " * Reached special r-value constructor.\n";
	}

    private:	
        MyClass& operator=(const MyClass& v);
    };
    
    const MyClass SomeFunctionA()
    {
	return MyClass();
    }
    
    MyClass&& SomeFunctionB()
    {
        return MyClass();
    }
}

int main(void)
{
    std::cout << "Construct a.\n";
    MyClass a;

    std::cout << "Construct c & d.\n";
    MyClass c = a;
    MyClass d(a);
    
    std::cout << "Construct e & f.\n";
    MyClass e = SomeFunctionA();
    MyClass f(SomeFunctionA());
    
    std::cout << "Construct g & h.\n";
    MyClass g = SomeFunctionB();
    MyClass h(SomeFunctionB());
}
