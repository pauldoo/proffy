#pragma once

class Exception {
public:
	Exception(const std::string&);
	std::string Message() const;
private:
	const std::string m_message;
};
