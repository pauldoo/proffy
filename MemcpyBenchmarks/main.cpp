#include <cstdlib>
#include <ctime>
#include <iostream>
#include <vector>

int main(void)
{
	const int bufferSizeInMegabytes = 50;
	const int repeats = 200;

	const std::vector<char> source(bufferSizeInMegabytes * 1024 * 1024);
	std::vector<char> destination(bufferSizeInMegabytes * 1024 * 1024);

	const clock_t startTime = clock();
	for (int i = 0; i < repeats; i++) {
		memcpy(&(destination.at(0)), &(source.at(0)), source.size());
	}
	const clock_t endTime = clock();

	const double timeInSeconds = (endTime - startTime) / static_cast<double>(CLOCKS_PER_SEC);
	const double gigabytesCopied = (bufferSizeInMegabytes * repeats) / 1024.0;

	std::cout << gigabytesCopied << " GiB copied in " << timeInSeconds << " s, " << (gigabytesCopied / timeInSeconds) << " GiB/s\n";

	return 0;
}
