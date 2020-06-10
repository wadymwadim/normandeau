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

char* check_square(const int64_t numerx, const int64_t numery, const int64_t denom,
                   const char* const code_pair_str, const char* const initial_angles_ptr,
                   const char* const cover_dir);

void free_string(const char* const str);
}
