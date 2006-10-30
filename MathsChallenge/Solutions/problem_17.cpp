// http://mathschallenge.net/index.php?section=project&ref=problems&id=17

#include <cassert>
#include <iostream>
#include <sstream>

namespace {
    const std::string ToString(const int num)
    {
        if (num == 1000) {
            return "one thousand";
        } else if (num >= 100) {
            const std::string prefix = ToString(num / 100) + " hundred";
            const std::string suffix = ToString(num % 100);
            if (suffix.empty()) {
                return prefix;
            } else {
                return prefix + " and " + suffix;
            }
        } else if (num >= 20) {
            switch (num / 10) {
                case 2:
                    return "twenty-" + ToString(num % 10);
                case 3:
                    return "thirty-" + ToString(num % 10);
                case 4:
                    return "forty-" + ToString(num % 10);
                case 5:
                    return "fifty-" + ToString(num % 10);
                case 6:
                    return "sixty-" + ToString(num % 10);
                case 7:
                    return "seventy-" + ToString(num % 10);
                case 8:
                    return "eighty-" + ToString(num % 10);
                case 9:
                    return "ninety-" + ToString(num % 10);
                default:
                    assert(false);
            }
        } else if (num < 20) {
            switch (num) {
                case 0:
                    return "";
                case 1:
                    return "one";
                case 2:
                    return "two";
                case 3:
                    return "three";
                case 4:
                    return "four";
                case 5:
                    return "five";
                case 6:
                    return "six";
                case 7:
                    return "seven";
                case 8:
                    return "eight";
                case 9:
                    return "nine";
                case 10:
                    return "ten";
                case 11:
                    return "eleven";
                case 12:
                    return "twelve";
                case 13:
                    return "thirteen";
                case 14:
                    return "fourteen";
                case 15:
                    return "fifteen";
                case 16:
                    return "sixteen";
                case 17:
                    return "seventeen";
                case 18:
                    return "eighteen";
                case 19:
                    return "nineteen";
                default:
                    assert(false);
            }
        } else {
            assert(false);
        }
        throw std::string("Not possible");
    }
}

int main(void)
{
    std::ostringstream buf;
    for (int i = 1; i <= 1000; i++) {
        buf << ToString(i) << "\n";
    }
    std::string str = buf.str();
    int c = 0;
    for (std::string::const_iterator i = str.begin(); i != str.end(); ++i) {
        if (*i >= 'a' && *i <= 'z') {
            c++;
        }
    }
    std::cout << c << "\n";
    return 0;
}

