This README starts with running instructions, then an older README follows which is more detailed but also somewhat outdated.

# How to Run

We include a minimal description here of the commands required to compile and run this program. Below, in the older README there are detailed descriptions for where these commands come from. The command install a number of tools and libraries required to compile the program. We walk through the setup here from start to running.

## Setup on Linux

This description targets Debian based distributions with the apt package manager. It should be relatively straightforward to adapt it to other distributions.

Fetch dependencies with `apt`:
```
$ sudo apt update
$ sudo apt install libgmp-dev libmpfr-dev libmpfi-dev libboost-all-dev libeigen3-dev libtbb-dev libjemalloc-dev libsqlite3-dev
```

(Naturally, if you want to get the code with git, also run `$ sudo apt install git-all`.)

The project requires java and javafx version 8. These are not supported anymore/at this time (ubuntu [bug report](https://bugs.launchpad.net/ubuntu/+source/openjfx/+bug/1799946)), which is why we didn't download java or javafx with `apt`.
There are a number of options here, we simply choose one convenient one here. We include instructions to install zulu-fx version 8. We install it through [sdkman](https://sdkman.io/) (a software development kit manager) to make managing java installations manageable.

```
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 8.0.252.fx-zulu
```

(Not needed for this project but you normally would want the default up-to-date java version `sdk install java` as well. Normally I'd set this version to be the default as well.)

## Get billiards code

To fetch the code with git,
```
$ git clone git@github.com:szepi1991/billiards.git
```
otherwise you can download it from github as a zip.

Some of the contents have been compressed since they take significantly less space that way. Instructions to enter the directory and extract these files.
```
$ cd billiards
$ cat split_coversfolder0* > coversfolder.zip
$ unzip coversfolder.zip
```

Later on if you want to update your local code to the latest changes in github, simply run `git pull`.

## Running the program

All required setup is complete. Go to the billiards directory if not already there.

Run `sdk env` to ensure the that the java version is switched to the correct one. Then simply run

```
$ ./gradlew run
```

which will compile and run the program.

# Older README 

This project has essentially two components: the computational backend that calculates the equations, and the graphical front end that displays the results.

The computational side of this project is written in C++, which is a language perfectly suited for high performance mathematical computation. The graphical interface is written in Java, which has the very nice JavaFX GUI framework.

I'm toying with the idea of rewriting the GUI part of the project in Qt5 to have a unified C++ codebase, but that would take a long time. If we ever run into computational limits with Java, we might consider this.

## Operating Systems

The code has been developed with a Unix operating system and environment in mind. I personally used a Mac, but any Linux distro should work too (note that George uses Macs though, so you still need to be familiar with them). It might be possible to get this code to work on Windows, but I don't even want to think about the mess that would entail. If you ever want to run this on a Windows machine, it's probably easier just to install Linux side-by-side (perhaps on a separate partition or in a VM, like VirtualBox) and run it on that (check out [Fedora](https://getfedora.org) for a nice modern Linux distro). The rest of this tutorial shows how to setup the work environment from scratch on a Mac, Ubuntu, or Fedora.

## Basic Setup

This readme assumes a basic familiarity with the command line. If you are unfamiliar, please read [this](https://www.learnenough.com/command-line-tutorial) or another tutorial. (Pleeeeeez read all of it. I know you just want to dive in and start working on things, but your life will be sooooo much easier and things will make sooooo much more sense if you finish the tutorial. Take things slowly. You will thank yourself.) I recommend using [iTerm2](https://www.iterm2.com) for your terminal emulator instead of the default Terminal.

## Package Manager

On a Mac, the easiest way to get one is to install the Command Line Developer Tools. Simply run

```
$ xcode-select --install
```

and then click `Install` on the pop-up window. This installs the Clang compiler (and various other tools).

Aside from the C++ and Java compilers, the rest of the tools we need can be installed with a system package manager. The luckly Linux folks have one built in, but on a Mac, we need to install one ourselves. The best one out there is [Homebrew](http://brew.sh). Follow the installation instructions on that webpage, and familiarize yourself with basic Homebrew commands (`man brew` is a good place to start). Don't forget to run `brew upgrade` from time-to-time to update your software.

## C++

This project uses C++ and so requires a C++ compiler.

On a Mac, the Command Line Developer Tools come bundled with the Clang compiler, so if you followed the above step, there isn't anything to do here.

I've also tested this project with GCC, which is the default on Linux.

This project uses C++14.

## Java

This project uses Java 8. Again, Java 9 is coming out in 2017 (I hope), so you can upgrade to that when it happens. We don't have any backwards compatability constraints here, so feel free to use the latest and greatest! There are two versions of Java, the JRE and the JDK. The former is for consumers who only want to run Java code, and the latter is for developers (us) who want to compile it. Getting these two mixed up has caused a lot of trouble for me (eg. some questions on Stack Overflow deal with one and not the other), so be aware of the difference. There are also different "flavors" of Java that come packaged in different ways. The standard one is OpenJDK, which you lucky Linux folks can just get from your package manager (you will likely also need to grab JavaFX, which we use too). On Mac, we have to use Oracle's bundled package, though feel free to ditch it for OpenJDK if it ever makes it into Homebrew (but don't use Homebrew cask, it just installs the Oracle version and has caused me a lot of problems). Go to the [Oracle](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) website (the link may change in the future, so poke around if the webpage doesn't work), accept the license agreement, and install the Mac OS X one (the demos and samples are optional). Follow all the instructions, and there you go (you may also want to go to the Java Control Pane and disable Java in the browser for security reasons).

```
$ sudo apt install openjdk-8-jdk openjfx
$ sudo dnf install
```

## Version Control

This project uses [Mercurial](https://www.mercurial-scm.org) for its version control system. On a Mac, the best way to install it is to install Python 2 using Homebrew, and then install Mercurial using Pip, which is the package manager for Python.

```
$ brew install python
$ pip2 install mercurial
```

You can run `pip2 list --outdated` to check for outdated packages, and then update them.

On Linux, just install mercurial using the default package manager.

```
$ sudo apt install mercurial
$ sudo dnf install mercurial
```

Now that Mercurial is installed, please familiarize yourself with the [basics](http://hginit.com) of Mercurial before continuing.

The code repository is stored on BitBucket. Ask George for the account name and password, and then you can clone the project to your computer, work on it from there, push changes back to BitBucket, and then pull them onto George's computer. (If you've read the tutorial, this will all make perfect sense. If not, then please go back and read it!)

Before every commit, it is a good idea to ensure that the code compiles and passes all the tests. To do this, add the following to the `.hg/hgrc` file in your cloned Mercurial repository. This also runs a formatter over the code, `clang-format`, after each commit, to ensure a consistent style (`brew install clang-format`). The formatting script uses the `fish` shell, which you can also install using Homebrew (and is also a very nice default shell, BTW).

```
[hooks]
# These hooks are executed in the order they are defined.
# This is important! You should only do formatting if the project compiles
# correctly, because syntax errors can really mess up the formatter
precommit.javabuild = gradle build
precommit.format = fish format.fish
```

## C++ Libraries

The application depends on the following libraries.

- [GMP](https://gmplib.org): Arbitrary precision arithmetic library for integers and rational numbers.
- [MPFR](http://www.mpfr.org): Multiprecision library for floating point numbers.
- [MPFI](https://perso.ens-lyon.fr/nathalie.revol/software.html): Multiprecision library for interval arithmetic.
- [Boost](http://www.boost.org): General utilities for C++. If you are looking for any C++ libraries or utilities, look here first. One of the most important libraries for us is the Multiprecision collection, which provides C++ wrappers around the previous three numerical libraries (which are written in C).
- [Eigen](http://eigen.tuxfamily.org/index.php?title=Main_Page): Template-based linear algebra.
- [Thread Building Blocks](https://www.threadingbuildingblocks.org/): Parallelism library.
- [Jemalloc](http://jemalloc.net/): High performance memory allocator. Our program uses a decent amount of memory for the multiprecision arithmetic calculations, and using this allocator improves the performance of the application by about 15% over the system allocator.

Let's install these using the package manager.

```
$ brew install gmp mpfr mpfi boost eigen tbb jemalloc
$ sudo apt install libgmp-dev libmpfr-dev libmpfi-dev libboost-all-dev libeigen3-dev libtbb-dev libjemalloc-dev
$ sudo dnf install tbb-devel
```

## Setting Up the Database

This program uses SQLite to store geometric information about the code sequences. Macs come with a version of SQLite bundled with the Command Line Developer Tools, so there is nothing to install for

```
$ sudo apt install libsqlite3-dev
$ sudo dnf install sqlite-devel
```

## Build Systems

### Gradle

```
$ brew install gradle
```

After each update of gradle, re-run `gradle wrapper` to update the wrapper to the latest version of gradle.


### Meson

To setup meson

```
$ mkdir meson
$ cd meson
$ meson.py ..
```

having a static analysis build would be nice
as well as profile guided optimization the code

## Eclipse

Hg plugin for eclipse


## Profiling

```
$ valgrind --tool=callgrind meson/profile  # creates file callgrind.out.x, where x is some number
$ gprof2dot -f callgrind -s callgrind.out.x | dot -Tpdf -o profile.pdf
```

## Design Choices

- Boost: a collection of some of the best libraries for C++. Whenever you are looking for a library for C++, look here first. Of particular interest are the Multiprecision and Interval Arithmetic collections. If you are looking for C++ wrappers of GMP, MPFR, and MPFI, here is a good place.
- Ginac: this is a symbolic mathematics library. These sorts of libraries are difficult to make, and Ginac seems to be the best (and perhaps only) one in C++ land. I ended up writing my own symbolic math code instead, which drastically increased the performance and simplified the code, so using Ginac is unnecessary.
- CLN: an arbitrary precision arithmetic library built into Ginac.
- CGAL: the most advanced and de facto computational geometry library for pretty well anything, not just C++.
- Arb:
- Flint:

The graphical front end on the other hand could be written in a variety of languages. For example,

- Python. Python has no native GUI library (one written in Python), but has bindings to essentially every GUI library out there. However,
- Java. Java has a long history of graphical libraries, going back to AWT and Swing. The modern one is JavaFX, which is very easy to use and also quite powerful.
- C++. C++ has the most powerful and advanced GUI libraries out there, most notably GTK and Qt. However, they are difficult to use and get right (as pretty well all of C++ is).

Java, on the other hand, is not meant for mathematical and numerical computation. It is a fast language, but its JIT compiler nature will never have the performance of AOT languages, and the intermediate compilation into bytecode makes optimizatin difficult. As such, it has has comparatively fewer mathematical libraries. Here are the better ones.

- Apache Commons Math: your one stop shop for numerical computation. Any time you are trying to do something numerically, look here first.
- Java Algebra System: library for polynomial algebra. Count your lucky stars if a problem ever deals with polynomials (espectially ones with rational coefficients), because they are extremely well behaved and understood.

Other useful libraries

- Guava: not a mathematical library, but the immutable collections are a welcome relief from the mutability of the built in Java collections

You will notice there is no general symbolic math library in this list. That's because there are no decent ones for Java. I briefly tried one called Symja, but it was terrible: terribly designed with terrible documentation and terrible performance. The lack of such a library is the single largest disadvantage of using Java. I made my own that is specially suited for our problems.

I also tried a library called Apfloat, which has arbitrary precision integers and floating point numbers. However, in some Java Microbenchmark Harness (JMH) benchmarks it was consistently beaten by the built in BigInteger and BigDecimal. However, this may be becasue BigInteger and BigDecimal to have an upper bound to how large they can be (though it is very large), while Apint is truly without an upper bound.

Another omission is interval arithmetic. Interval arithmetic allows you to deal rigourously with floating point calculations.

However, Java does have several advantages:

- It is much easier to use and safer than C++. C++ is probably the most complicated programming language on Earth, and has very few safety features. Java will catch you when you make a mistake; by default, C++ will not. This is the terrible scourge of undefined behaviour, and you must be vigilant to avoid it at all costs.
- Distributing Java applications is much easier. In C++, you have to worry about compilers, libraries, linking, Windows (ugh), etc. Distributing Java code on the other hand is effortless.

## In the Future

C++ and Java are not great, but at the moment (2016) they get the job done. However, on the horizon are two languages that could eat C++ and Java's lunch once they are more mature. Those languages are Rust and Kotlin. In a few years from now, I think it would be very benificial to rewrite the C++ part of the project in Rust, and the Java part in Kotlin. The Rust part may be a bit ambitious, but Kotlin should be very doable. (Another alternative for Java is Scala. Perhaps also check that out when Scala 3 comes out.)

Also, the build system should be revamped. Right now we are using Gradle for Java and Meson for C++. I'd like to have one build system that handles both. Gradle support for C++ isn't the greatest now, but if it improves (say, in Gradle 4), feel free to switch back to that. Also, maybe check out Bazel once it hits 1.0. In the meantime, I believe there are significant changes coming to Gradle in version 4 (a new components thing, and Kotlin-script too), so feel free to update that if there are no better build systems by that time.

## Books

Oracle has some online tutorials about Java [here](https://docs.oracle.com/javase/tutorial/). They're very comprehensive, but could be better explained in some places. There is also a list of books [here](https://www.quora.com/What-are-the-best-books-to-learn-Java). Don't forget you can access a lot of these books online through the university library website.

There aren't a whole lot of JavaFX resources out there. The best book I found is *Learn JavaFX 8: Building User Experience and Interfaces with Java 8* by Kishori Sharan. Other interesting JavaFX libraries are ReactFX and RxJava. Make sure you look for JavaFX 8 tutorials (not JavaFX 2 or 2.2, since much has changed since then).

For C++, Stackoverflow has a comprehensive list [here](http://stackoverflow.com/questions/388242/the-definitive-c-book-guide-and-list).

SQLite has good documentation on its [website](https://www.sqlite.org/docs.html).

## Eclipse

In practice, you can use whatever editor or IDE you like to develop the code. I for example use neovim. One option is to use Eclipse, which is what George uses. Here are the instructions for setting up Eclipse for our project:

Eclipse

Go to Eclipse Project Downloads
Download the Platform Runtime Binary for your platform
Install the Marketplace
From the marketplace, install
- JDT
- CDT
- Buildship
- Git
- Mercurial

## Libraries

Look at awesome-java and awesome-cpp. Also most things in Boost.

## Ubuntu Libs

mercurial

Very important article
https://en.wikipedia.org/wiki/Polynomial_greatest_common_divisor#Euclidean_division

Alright, so the problem is this: We have some big sin equation f, and we want to check if sin(x) is a factor, and if so, how many times is it a factor. We can translate this into a question about polynomials. So we want to find out the remainder of polynomial division, I think. Or, some way of determining
