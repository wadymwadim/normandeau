#pragma once

#include <cstdint>

struct CCodeInfo {
    char* points;
    char* sin_equations;
    char* cos_equations;
};

extern "C" {
int32_t load_code_info(const char* const code_numbers_ptr,
                       const char* const initial_angles_ptr,
                       CCodeInfo* const c_code_info);
void cleanup_code_info(const CCodeInfo* const c_code_info);
}
