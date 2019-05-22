namespace Model 
{
public class Address 
{
    // We need and id what about two people living in the same palace.
    [MaxLength(10)]
    public string Id { get; set; }
    [MaxLength(200)]
    public string Direction { get; set;}
    public Country Country { get; set;}
    [MaxLength(20)]
    public string Zip { get; set;}
    [MaxLength(200)]
    public string City { get; set;}
    public Province  Province { get; set;}
}
}