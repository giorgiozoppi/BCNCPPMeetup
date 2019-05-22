using System.ComponentModel.DataAnnotation;

namespace Model 
{
public class Country
{
    [StringLength(5)]
    public string Code { get; set;}
    [StringLength(250)]
    public string Name { get; set; }
}
}