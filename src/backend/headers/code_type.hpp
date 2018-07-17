#pragma once

#include <ostream>

enum class CodeType : uint64_t {
    OSO,  // open stable odd
    OSNO, // open stable not odd
    ONS,  // open not stable
    CS,   // closed stable
    CNS,  // closed not stable
};

std::ostream& operator<<(std::ostream& os, const CodeType code_type);

bool is_stable(const CodeType code_type);
