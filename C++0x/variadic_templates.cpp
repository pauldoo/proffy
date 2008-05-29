#include <iostream>

namespace {
  template<typename T>
  void PrintStuff(const T& arg)
  {
    std::cout << arg << "\n";
  }

  template<typename First, typename... Rest>
  void PrintStuff(const First& first, Rest... args)
  {
    PrintStuff(first);
    PrintStuff(args...);
  }
}

int main(void)
{
  PrintStuff(10, "Hi", 45.2);
  return 0;
}
