#include "External.h"
#include "LRC.h"

#include "RollingChecksum.h"

LRC::LRC(std::ostream* output) : m_output(output)
{
}

LRC::~LRC()
{
}

void LRC::WriteStream(std::istream&)
{
}

