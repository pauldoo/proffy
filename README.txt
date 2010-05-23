About:

    Proffy is free software for profiling native code applications under
    Windows.

    * Sampling only, no intrusive instrumentation.

    * Call graph profiling.

    * GUI-less. Include a single C++ header file into your application to
    control exactly when the profiler starts and stops.

    * Results saved to XML (viewable in your browser) and Graphviz "dot".

    * Can profile multi-threaded applications.

    Created by Paul Richards and released as free software under the GPLv3.

    http://pauldoo.com/proffy/


Instructions:

    Add #include <C:/path/to/Proffy/Proffy/Launcher.h>

    Decorate the function to be profiled like this (note the {} braces for
    scoping of Proffy::Launcher variable):

    {
        Proffy::Launcher profiler(
            L"C:/path/to/Proffy/bin64/Proffy64.exe", // Path to Proffy.exe
            L"C:/path/to/Proffy", // Output directory for the result files
            1.0 / 20); // Delay between samples, so here set to 20 Hz

        SomeFunctionWhichWillGetProfiled();

        // The Proffy::Launcher destructor will cause the profiler to stop and save
        // the report.  So ensure you have the scope correct.
    }
    exit(0); // Optional statement just to end the program we are profiling

    After running the application copy the "Xhtml.xsl" file into the output
    directory, then open the XML files in Mozilla Firefox or Google Chrome
    (Microsoft Internet Explorer is currently not supported).

    In addition some "dot" files are generated which can be by GraphViz to
    generate visual callgraphs. These are experimental, so please give feedback
    on their usefulness.


Frequently asked questions:

    I get very little readable output.

        Ensure that debug symbols are being generated for the code you wish to
        profile.

        Also take care to ensure that frame pointers are present in the
        application being profiled. If these are omitted (due to optimization
        settings) then the results from the profiler will be extremely poor. The
        MSVC flag to disable frame pointer omission is "/Oy-".

    Can I mix and match the 32/64-bit versions of Proffy with 32/64-bit
    applications?

        The safest option is to always use the 32-bit version of Proffy with
        32-bit applications, and the 64-bit version of Proffy with 64-bit
        applications.

        Sometimes the builds can be mixed (ie a 32-bit version of Proffy on a
        64-bit application, or visa versa), but this seems to be sensitive to
        unknown environmental differences. I would like to hear your experiences
        with this.

    I get a lot of "symbol file could not be found" errors.

        These indicate that Proffy was unable to find debug symbols for parts of
        the application and will not produce detailed profiling output. These
        are only important if they pertain to code that you wish to profile, in
        which case you should locate or generate debugging symbols.

    Do you have advice for sampling rate?

        As the sampling rate increases the impact of the profiler on the
        application will increase. I recommend setting the sampling rate to no
        higher than 10 or 20 Hz. This figure does not come from a scientific
        evaluation.

    Do you have advice for minimum runtime to get accurate results?

        To get accurate results I recommend that a minimum of several hundred
        samples should be taken (but more is better). Coupled with a maximum
        sampling rate of 20 Hz recommended above, this implies in a runtime of
        at least 30 seconds or so for accurate results.


External dependencies:

    This project contains files copied from:

    Microsoft Debugging Tools for Windows (v6.12.2.633).

    Apache Xerces-C++ (v3.0.1).


Future to-do list:

    In rough order of priority, from highest to lowest:

    * Remove dead code added during initial exploration of DbgEng.

    * Port to C++/CLI.

    * Investigate better symbol searching (e.g. MS symbol server). Find the
    outer source file which you want to profile and:

