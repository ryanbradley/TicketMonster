h1. What is this?

Ticket Monster is an application that demonstrates a few of the capabilities of JBoss AS. In this repository you will
find a Spring-based implementation.

h1. Setup

In order to set up the database, add a datasource to the application as follows:

a) edit the file $JBOSS_HOME/standalone/configuration/standalone.xml

b) search for the element `<subsystem xmlns="urn:jboss:domain:datasources:1.0">`

c) add the following sub-element to it:

    <datasource jndi-name="java:jboss/datasources/TicketMonsterDS" pool-name="TicketMonsterDS" enabled="true" jta="true" use-java-context="true" use-ccm="true">
                    <connection-url>
                        jdbc:h2:mem:test;DB_CLOSE_DELAY=-1
                    </connection-url>
                    <driver>
                        h2
                    </driver>
                    <pool>
                        <prefill>
                            false
                        </prefill>
                        <use-strict-min>
                            false
                        </use-strict-min>
                        <flush-strategy>
                            FailingConnectionOnly
                        </flush-strategy>
                    </pool>
                    <security>
                        <user-name>
                            sa
                        </user-name>
                        <password>
                            sa
                        </password>
                    </security>
                    <validation>
                        <validate-on-match>
                            false
                        </validate-on-match>
                        <background-validation>
                            false
                        </background-validation>
                        <useFastFail>
                            false
                        </useFastFail>
                    </validation>
                </datasource>