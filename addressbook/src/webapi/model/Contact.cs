namespace Model
{
class Contact
{
    public string ContactId { get; set;}
    public string Name { get; set;}
    public string Surname { get; set; }
    public string AddressId { get ; set; }
    public Address Address { get; set; }
    
}
}