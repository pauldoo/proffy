#include <iostream>
#include <list>
#include <map>
#include <sstream>
#include <string>
#include <vector>

namespace {
    class Markov
    {
    public:
        Markov(const unsigned int prefix_length) : m_prefix_length(prefix_length)
        {
        }
        
        ~Markov()
        {
        }
    
        void AccumulateInput(std::istream& in)
        {
            Prefix prefix;
            while (true) {
                std::string word;
                in >> word;
                if (!in) break;
                if (prefix.size() == m_prefix_length) {
                    AddSuffix(prefix, word);
                    prefix.pop_front();
                }
                prefix.push_back(word);
            }
            if (prefix.size() != m_prefix_length) {
                throw std::string("Input not long enough");
            }
            AddSuffix(prefix, "\n");
        }
        
        void GenerateOutput(std::ostream& out)
        {
            if (m_start_prefix.empty()) {
                throw std::string("Suitable data not loaded");
            }
            Prefix prefix = m_start_prefix;
            for (Prefix::const_iterator i = prefix.begin(); i != prefix.end(); ++i) {
                out << (*i) << " ";
            }
            while (true) {
                const SuffixList& suffixes = m_suffix_map[prefix];
                const std::string& word = suffixes[random() % suffixes.size()];
                out << word;
                if (word == "\n") {
                    return;
                }
                out << " ";
                prefix.pop_front();
                prefix.push_back(word);
            }
        }
        
    private:
        typedef std::list<std::string> Prefix;
        typedef std::vector<std::string> SuffixList;

        const unsigned int m_prefix_length;
        Prefix m_start_prefix;
        std::map<Prefix, SuffixList> m_suffix_map;
        
        void AddSuffix(const Prefix& prefix, const std::string& word)
        {
            if (prefix.size() != m_prefix_length) {
                throw std::string("Invalid suffix length");
            }
            if (m_start_prefix.empty()) {
                m_start_prefix = prefix;
            }
            
            m_suffix_map[prefix].push_back(word);
        }
    };
}

int main(const int argc, const char* argv[])
{
    try {
        std::ios::sync_with_stdio(false);
        srandomdev();
        if (argc != 2) {
            std::clog << "Usage:" << std::endl;
            std::clog << argv[0] << " [prefix length]" << std::endl;
            return EXIT_FAILURE;
        }
    
        std::stringstream prefix_length_stream(argv[1]);
        unsigned int prefix_length = 0;
        prefix_length_stream >> prefix_length;
        
        Markov markov(prefix_length);
        markov.AccumulateInput(std::cin);
        markov.GenerateOutput(std::cout);
        return EXIT_SUCCESS;
    } catch (const std::string& error) {
        std::cerr << error << std::endl;
        return EXIT_FAILURE;
    }
}
