# Granting Permissions using Capabilities (Linux)

If your server is running Linux you can use this plugin as a non-root user by granting Capabilities to your java executable.
In particular `CAP_NET_RAW` and `CAP_NET_ADMIN` are required for managing network interfaces.

To grant them run this command as root:
```bash
setcap cap_net_raw,cap_net_admin=eip /path/to/java
```

### ⚠️ Java Fails to Load libjli.so After Granting Capabilities
After granting the capabilities you might see this error when running java:
```
java: error while loading shared libraries: libjli.so: cannot open shared object file: No such file or directory
```

This happens because `ld.so` will refuse to link libraries from untrusted paths when your executable is executed with elevated priviliges.
This is a known JDK Bug ([JDK-7157699](https://bugs.openjdk.org/browse/JDK-7157699)).
To fix this we need to make sure the path of `libjli.so` is part of the trusted system library path.

### Step 1: Locate the Library path

Run the following command to find where Java is looking for shared libraries:
```bash
ldd /path/to/java
```
Look for line like:
```bash
libjli.so => /path/to/jdk/lib/libjli.so
```

### Step 2: Register the Path with the linker

Create a new file under `/etc/ld.so.conf.d/` called `java.conf`

Add the directory containing libjli.so to it:
```
/path/to/jdk/lib/
```

Save and exit, then update the linker cache by running this as root:
```bash
ldconfig
```

To verify that it has been recognized you can run:
```bash
ldconfig -p | grep libjli
```
You should be seeing a line of output