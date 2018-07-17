#include "code_type.hpp"
#include "util.hpp"

std::ostream& operator<<(std::ostream& os, const CodeType code_type) {
    switch (code_type) {
    case CodeType::OSO:
        return os << "OSO";
    case CodeType::OSNO:
        return os << "OSNO";
    case CodeType::ONS:
        return os << "ONS";
    case CodeType::CS:
        return os << "CS";
    case CodeType::CNS:
        return os << "CNS";
    }

    throw std::runtime_error(invalid_enum_value("CodeType", code_type));
}

bool is_stable(const CodeType code_type) {
    switch (code_type) {
    case CodeType::OSO:
    case CodeType::OSNO:
    case CodeType::CS:
        return true;
    case CodeType::ONS:
    case CodeType::CNS:
        return false;
    }

    throw std::runtime_error(invalid_enum_value("CodeType", code_type));
}
