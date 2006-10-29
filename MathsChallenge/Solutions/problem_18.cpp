// http://mathschallenge.net/index.php?section=project&ref=problems&id=18

#include <iostream>
#include <vector>

int main(void)
{
    std::vector<int> numbers;
    int row = 0;
    int col = 0;
    int index = 0;
    int max = 0;
    
    while (true) {
        int n;
        std::cin >> n;
        if (!std::cin) {
            break;
        } else {
            //std::cout << n << " (" << row << " " << col << ")";
            if (row == 0) {
                numbers.push_back(n);
            } else {
                int a = -1;
                int b = -1;
                if (col > 0) {
                    // get top left
                    a = numbers[index - (row + 1)];
                }
                if (col < row) {
                    // get top right
                    b = numbers[index - row];
                }
                //std::cout << " (" << a << " " << b << ")\n"; 
                n = std::max(n + a, n + b);
                numbers.push_back(n);
                max = std::max(max, n);
            }
            
            index++;
            col++;
            if (col > row) {
                row++;
                col = 0;
            }
        }
    }
    std::cout << max << "\n";
    return 0;
}

