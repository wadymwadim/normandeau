#include "code_type.hpp"
#include "util.hpp"

std::ostream& operator<<(std::ostream& os, const CodeType code_type) {

    switch (code_type) {
    case CodeType::SON:
        return os << "SON";
    case CodeType::SEP:
        return os << "SEP";
    case CodeType::SEN:
        return os << "SEN";
    case CodeType::UEP:
        return os << "UEP";
    case CodeType::UEN:
        return os << "UEN";
    }

    throw std::runtime_error(invalid_enum_value("CodeType", code_type));
}

bool is_stable(const CodeType code_type) {

    switch (code_type) {
    case CodeType::SON:
    case CodeType::SEP:
    case CodeType::SEN:
        return true;
    case CodeType::UEP:
    case CodeType::UEN:
        return false;
    }

    throw std::runtime_error(invalid_enum_value("CodeType", code_type));
}
