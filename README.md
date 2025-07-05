# Belema's Liver Span Classifier

A simple Python GUI application that uses **Belema's Law of Hepatic-Height Correlation** to estimate normal liver span based on height and age. The app flags hepatomegaly using individualized thresholds.

---

## 🔬 Background

Based on:

> **Belema's Law:**  
> Liver span (cm) = 12.6 × height (m) − offset  
> - Offset = **5.04** if age ≥ 12  
> - Offset = **3.00** if age < 12  
>
> _Ibiabuo, P. (2024). Hepatic Morphometric Correlation with Biophysical Profiling of Height.  
> Journal of Biosciences and Medicines, 12, 194-206. doi: [10.4236/jbm.2024.127018](https://doi.org/10.4236/jbm.2024.127018)_

---

## 💡 Features

- Predicts liver span based on height and age
- Classifies hepatomegaly:
  - ±2 cm = Normal
  - +2–3 cm = Mild hepatomegaly
  - +3–4 cm = Moderate hepatomegaly
  - >4 cm = Severe hepatomegaly
- Exports results as a PDF
- Easy GUI built with Tkinter

---

## 📦 Requirements

- Python 3.x
- Tkinter (built-in)
- `reportlab` for PDF export:
  ```bash
  pip install reportlab