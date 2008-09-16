# Here are some convenience commands that you can use to run the XmlRoundTrip class and the GenerateRandomXml. These use
# the mvn command line to exec specific main methods, thus cirucmventing the need to mess about with classpaths.

# Use the following commands from within the fuge-jaxb2 sub-directory:

# Create 3 randomly-generated (but with identifier/identifier references intact) XML files based on the FuGE Version 1 XSD.
# This program would need to be modified if using a community extension XSD instead.
mvn exec:java -Dexec.mainClass="net.sourceforge.fuge.util.GenerateRandomXml" -Dexec.args="src/main/resources/xmlSchema.xsd output.xml"

# Input and then output an XML file based on the FuGE Version 1 XSD.
# This program would need to be modified if using a community extension XSD instead.
mvn exec:java -Dexec.mainClass="net.sourceforge.fuge.util.XmlRoundTrip" -Dexec.args="src/main/resources/xmlSchema.xsd output0.xml roundtrip.xml"

# To add svn properties to a bunch of files (you may wish to change the extension)
find . \( -name '.svn' -prune \) -o -name '*.java' -exec svn propset svn:keywords "Date Rev Author HeadURL" {} \;
