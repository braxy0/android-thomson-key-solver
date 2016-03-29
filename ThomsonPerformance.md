# Current Status for the Thomson Calculator (as of rev. 110) #

We only state the speed for this one as this is the most demanding algorithm. The others are pretty simple to implement.

### Generator speed ( on a Intel Core2Duo E8200 @ 2.66GHz ) ###

Stage 1: ~16 secs  - Hashing of the possible networks

Stage 2: ~11 secs  - Sorting the possible networks

Stage 3: ~1 sec   - Creating Secondary tables

Stage 4: ~300 msecs   - Creating Main table

Stage 5: ~300 msecs - Creating cfv file

### Solver Speed ( on Gigabyte GSmart G1305 with a Qualcomm MSM 7227 600 MHz processor clocked at 480 MHz ) ###
  * 70 ms ( min ) - 100 ms ( typ ) - 170 ms (max)

As we can see from these results the noise of the measurement is almost as big as the calculation itself and that is a sign the calculation is fast enough. :)
Also the results apply to any of the supported dictionary versions.

**Recent Improvements in Speed**

From rev.19 to rev.22: from 13 seconds to 200 milliseconds ( 98% improvement)

From rev.27 to rev.30: from 200 milliseconds to 100 milliseconds( 50% improvement)

**Recent Improvements in Size**

From rev.27 to rev.30: from 69.4 megabytes to 55.8 megabytes ( 22% improvement)

From rev.90 to rev. 92: from 58.8 megabytes to 41.9 megabytes ( 24% improvement)

From rev.108 to rev. 110: from 41.9 megabytes to 28.0 megabytes ( 33% improvement)

Dictionary size : 28.0 MB in one file