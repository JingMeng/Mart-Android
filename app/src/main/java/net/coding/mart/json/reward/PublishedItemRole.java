
package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class PublishedItemRole implements Serializable {

    private static final long serialVersionUID = -1981921540480486055L;

    @SerializedName("typeId")
    @Expose
    public int typeId;
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("description")
    @Expose
    public String description = "";
    @SerializedName("totalPrice")
    @Expose
    public BigDecimal totalPrice;


}
