#include <iostream>

namespace {
    class MyClass
    {
    };
    
    bool is_r_value(MyClass&& v)
    {
        return true;
    }
    bool is_r_value(const MyClass&)
    {
        return false;
    }
}

int main(void)
{
    const MyClass a = MyClass();
    MyClass b = MyClass();

    std::cout << "a: " << is_r_value(a) << "\n";
    std::cout << "b: " << is_r_value(b) << "\n";
    std::cout << "MyClass(): " << is_r_value(MyClass()) << "\n";
}
