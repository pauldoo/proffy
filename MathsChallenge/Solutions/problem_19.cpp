// http://mathschallenge.net/index.php?section=project&ref=problems&id=19

#include <iostream>
#include <cassert>

namespace {
    const int DaysInMonth(const int month, const int year) {
        switch (month) {
            case 1:
                if ((year % 400) == 0 || ((year % 100) != 0 && (year % 4) == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            default:
                assert(false);
        }
        throw std::string("Boom");
    }
}

int main(void)
{
    int dayOfWeek = 1;
    int month = 0;
    int year = 1900;
    int count = 0;
    while (year <= 2000) {
        dayOfWeek += DaysInMonth(month, year);
        dayOfWeek %= 7;
        month ++;
        if (month >= 12) {
            year ++;
            month = 0;
        }
        if (dayOfWeek == 0 && year >= 1901) {
            count ++;
        }
    }
    std::cout << count << "\n";
}
