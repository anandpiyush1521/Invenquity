# SKU Code Generation Guide

This guide provides instructions for creating SKU (Stock Keeping Unit) codes for products in the inventory management system. Follow these guidelines to ensure consistency and uniqueness across the inventory.

---

## **1. General SKU Format**
The SKU code is structured as:
  ```
  <Category Abbreviation>-<Brand Abbreviation>-<Unique Feature/Specification>
  ```
### Example:  
For a Samsung 5G Smartphone:
  ```
   SMP-SM-5G
  ```

## **2. Abbreviation Guidelines**

### **a. Category Abbreviation**
The following table lists predefined abbreviations for common product categories:

| **Category Name**          | **Abbreviation** |
|-----------------------------|------------------|
| Smartphone                 | `SMP`            |
| Laptop                     | `LPT`            |
| Tablet                     | `TBL`            |
| Smartwatch                 | `SWT`            |
| Headphone                  | `HPH`            |
| Speaker                    | `SPK`            |
| Television                 | `TV`             |
| Camera                     | `CAM`            |
| Desktop Computer           | `DPC`            |
| Gaming Console             | `GMC`            |
| Keyboard                   | `KBD`            |
| Mouse                      | `MSE`            |
| Monitor                    | `MON`            |
| Printer                    | `PRN`            |
| Scanner                    | `SCN`            |
| Router                     | `RTR`            |
| External Storage (HDD/SSD) | `EXT`            |
| Drone                      | `DRN`            |
| Projector                  | `PJR`            |
| Smart Home Device          | `SHD`            |
| Air Conditioner (Smart AC) | `SAC`            |
| Refrigerator               | `RFR`            |
| Microwave                  | `MWV`            |
| Washing Machine            | `WSH`            |

> **Note**: For new categories, create a 3-character abbreviation that logically represents the category name.

### **b. Brand Abbreviation**
- Use the first **two characters** of the brand name in uppercase.  
Examples:
  - **Samsung** → `SM`  
  - **Apple** → `AP`  
  - **Dell** → `DL`

### **c. Unique Feature/Specification**
- Include a defining feature or specification, such as:
  - Connectivity Type: `5G`, `WIFI`
  - Storage Capacity: `256GB`
  - Series Name: `GM` (Gaming)

---

## **3. Rules for SKU Code Creation**

1. **Uniqueness**: Each SKU code must be unique to avoid conflicts.
2. **No Spaces or Special Characters**: Use hyphens (`-`) as delimiters. Avoid spaces, slashes, or other special characters.
3. **Case Sensitivity**: Always use uppercase letters.
4. **Mandatory Segments**: All three segments (category, brand, specification) must be included.

---

## **4. Examples of SKU Codes**

| **Product Description**                      | **SKU Code**       |
|----------------------------------------------|--------------------|
| Smartphone, Samsung, 5G                     | `SMP-SM-5G`        |
| Laptop, Dell, 16GB RAM                      | `LPT-DL-16GB`      |
| Headphone, Bose, Noise Cancelling           | `HPH-BS-NC`        |
| Gaming Console, Sony, PlayStation 5         | `GMC-SY-PS5`       |
| Monitor, LG, 27-inch                        | `MON-LG-27IN`      |
| Router, TP-Link, Dual-Band                  | `RTR-TPL-DB`       |
| Drone, DJI, 4K Camera                       | `DRN-DJ-4K`        |
| Refrigerator, LG, Double Door               | `RFR-LG-DD`        |

---

## **5. Steps for Creating SKU Codes**

1. **Determine Product Category**: Identify the category and select its abbreviation.
2. **Identify the Brand**: Extract the first two letters of the brand name.
3. **Choose a Unique Feature**: Select the key specification to add as the final segment.
4. **Combine Segments**: Concatenate the three segments using hyphens (`-`).
5. **Verify Uniqueness**: Ensure the SKU code does not already exist in the system.

---

## **6. Error Handling**
- **Duplicate SKU Codes**: Adjust the unique feature segment or add a version suffix (e.g., `V1`, `V2`).
- **Invalid Characters**: Prompt users to correct invalid inputs and reformat the SKU code.

---

## **7. Adding New Categories**
To introduce a new category:
1. Create a logical 3-character abbreviation.
2. Ensure it doesn’t conflict with existing abbreviations.
3. Update the category list in this guide.

---

This guide ensures that SKU codes remain consistent, descriptive, and easy to manage within the inventory system.
