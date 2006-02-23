#ifndef LRC_LRC
#define LRC_LRC

class LRC
{
    public:
        LRC(std::ostream* output);
	~LRC();

	void WriteStream(std::istream& input);
	
    private:
        std::ostream* const m_output;
};

#endif

