#pragma once

#include <ostream>

enum class CodeType {
    SON, // stable odd nonperp
    SEP, // stable even perp
    SEN, // stable even nonperp
    UEP, // unstable even perp
    UEN, // unstable even nonperp
};

std::ostream& operator<<(std::ostream& os, const CodeType code_type);

bool is_stable(const CodeType code_type);
