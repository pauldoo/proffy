INCLUDE_DIRECTORIES(${MathsChallenge_SOURCE_DIR})
LINK_DIRECTORIES(${MathsChallenge_BINARY_DIR}/Shared)

ADD_EXECUTABLE(problem_10 problem_10.cpp)
ADD_EXECUTABLE(problem_125 problem_125.cpp)
TARGET_LINK_LIBRARIES(problem_10 Shared)
TARGET_LINK_LIBRARIES(problem_125 Shared)

ADD_EXECUTABLE(problem_125_sean problem_125_sean.cpp)

