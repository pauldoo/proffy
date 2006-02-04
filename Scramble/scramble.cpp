#include <iostream>
#include <locale>
#include <string>

namespace
{
    void ShuffleAndPrint(std::string& word, std::ostream& out)
    {
        if (!word.empty()) {
            if (word.size() > 2) {
                std::random_shuffle(word.begin() + 1, word.end() - 1);
            }
            out << word;
            word.clear();
        }
    }
}

int main(void)
{
    std::ios::sync_with_stdio(false);
    srand(time(0));
    srandom(time(0));
    
    const std::ctype<char>& ctype = std::use_facet<std::ctype<char> >(std::cin.getloc());
    
    std::string word;
    while (true) {
        const char c = std::cin.get();
        if (!std::cin) break;
        if (ctype.is(std::ctype<char>::alpha, c)) {
            word.push_back(c);
        } else {
            ShuffleAndPrint(word, std::cout);
            std::cout << c;
        }
    }
    ShuffleAndPrint(word, std::cout);
    
    return EXIT_SUCCESS;
}

