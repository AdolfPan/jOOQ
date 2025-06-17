
package org.jooq.util.xml.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TableType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TableType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BASE TABLE"/&gt;
 *     &lt;enumeration value="VIEW"/&gt;
 *     &lt;enumeration value="MATERIALIZED VIEW"/&gt;
 *     &lt;enumeration value="GLOBAL TEMPORARY"/&gt;
 *     &lt;enumeration value="LOCAL TEMPORARY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TableType")
@XmlEnum
public enum TableType {


    /**
     * The table is an ordinary table, or a table whose type isn't recognised by jOOQ (yet).
     * 
     */
    @XmlEnumValue("BASE TABLE")
    BASE_TABLE("BASE TABLE"),

    /**
     * The table is a VIEW.
     * 
     */
    VIEW("VIEW"),

    /**
     * The table is a MATERIALIZED VIEW.
     * 
     */
    @XmlEnumValue("MATERIALIZED VIEW")
    MATERIALIZED_VIEW("MATERIALIZED VIEW"),

    /**
     * The table is a GLOBAL TEMPORARY table.
     * 
     */
    @XmlEnumValue("GLOBAL TEMPORARY")
    GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),

    /**
     * The table is a LOCAL TEMPORARY table.
     * 
     */
    @XmlEnumValue("LOCAL TEMPORARY")
    LOCAL_TEMPORARY("LOCAL TEMPORARY");
    private final String value;

    TableType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TableType fromValue(String v) {
        for (TableType c: TableType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    @Override
    public String toString() {
        switch (this) {
            case BASE_TABLE:
                return "BASE TABLE";
            case MATERIALIZED_VIEW:
                return "MATERIALIZED VIEW";
            case GLOBAL_TEMPORARY:
                return "GLOBAL TEMPORARY";
            case LOCAL_TEMPORARY:
                return "LOCAL TEMPORARY";
            default:
                return this.name();
        }
    }

}
