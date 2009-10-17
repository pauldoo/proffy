#ifndef COMMON_H
#define COMMON_H

typedef struct
{
    float* m_data;
    int m_width;
    int m_height;
}  FloatImage;

typedef struct
{
    short* m_data;
    int m_width;
    int m_height;
} ShortImage;

typedef struct
{
    FloatImage* m_images;
    int m_count;
} FloatVolume;

typedef struct
{
    ShortImage* m_images;
    int m_count;
} ShortVolume;

typedef struct
{
    double m_scale;
    FloatVolume* m_warp_x;
    FloatVolume* m_warp_y;
    FloatVolume* m_warp_z;
} Warpfield;

typedef void (*WarperFunc)(
    const ShortVolume* /* input_data */,
    const Warpfield* /* warpfield */,
    const ShortVolume* /* output_data */,
    const int iterations);

inline short* LookupShortImage(const ShortImage* const image, const int x, const int y)
{
    return image->m_data + x + y * image->m_width;
}

inline short* LookupShortVolume(const ShortVolume* const volume, const int x, const int y, const int z)
{
    return LookupShortImage(volume->m_images + z, x, y);
}

inline float* LookupFloatImage(const FloatImage* const image, const int x, const int y)
{
    return image->m_data + x + y * image->m_width;
}

inline float* LookupFloatVolume(const FloatVolume* const volume, const int x, const int y, const int z)
{
    return LookupFloatImage(volume->m_images + z, x, y);
}

void Bailout(const char* const message);

#endif
