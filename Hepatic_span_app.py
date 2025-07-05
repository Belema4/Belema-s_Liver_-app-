import tkinter as tk
from tkinter import ttk, messagebox, filedialog
from reportlab.pdfgen import canvas

class HepaticSpanApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Belema's Liver Span Predictor")
        self.root.geometry("500x400")
        self.root.resizable(False, False)

        self._setup_style()
        self._create_variables()
        self._build_ui()

    def _setup_style(self):
        style = ttk.Style()
        style.configure("TLabel", font=("Arial", 10))
        style.configure("TButton", font=("Arial", 10, "bold"))

    def _create_variables(self):
        self.name_var = tk.StringVar()
        self.height_var = tk.StringVar()
        self.age_var = tk.StringVar()
        self.result_var = tk.StringVar()
        self.interpretation_var = tk.StringVar()

    def _build_ui(self):
        ttk.Label(self.root, text="Patient Name (optional):").pack(pady=5)
        ttk.Entry(self.root, textvariable=self.name_var, width=30).pack()

        ttk.Label(self.root, text="Height (cm):").pack(pady=5)
        ttk.Entry(self.root, textvariable=self.height_var, width=30).pack()

        ttk.Label(self.root, text="Age (years):").pack(pady=5)
        ttk.Entry(self.root, textvariable=self.age_var, width=30).pack()

        ttk.Button(self.root, text="Predict Liver Span", command=self.predict_span).pack(pady=15)

        ttk.Label(self.root, textvariable=self.result_var, font=("Arial", 11, "bold")).pack(pady=5)

        self.result_label = ttk.Label(self.root, textvariable=self.interpretation_var, font=("Arial", 10, "italic"))
        self.result_label.pack(pady=5)

        ttk.Button(self.root, text="Export as PDF", command=self.export_to_pdf).pack(pady=10)

        ttk.Label(self.root, text="*Using Belema's Law of Hepatic-Height Correlation*", font=("Arial", 8), foreground="gray").pack(pady=10)

    def predict_span(self):
        try:
            height_cm = float(self.height_var.get())
            age = int(self.age_var.get())
            height_m = height_cm / 100

            # Updated age-based formula
            if age <= 12:
                predicted = 12.6 * height_m - 3.00
            else:
                predicted = 12.6 * height_m - 5.04

            predicted = round(predicted, 2)
            self.result_var.set(f"Predicted Liver Span: {predicted} cm")

            # Interpretation
            if predicted < 10:
                interp = "Smaller than average liver span – consider atrophy"
                color = "gray"
            elif 10 <= predicted < 17:
                interp = "Normal liver span for this individual"
                color = "green"
            elif 17 <= predicted < 18:
                interp = "Mild hepatomegaly – monitor clinically"
                color = "orange"
            elif 18 <= predicted <= 20:
                interp = "Moderate hepatomegaly – consider further evaluation"
                color = "red"
            else:
                interp = "Severe hepatomegaly – urgent clinical attention"
                color = "darkred"

            self.interpretation_var.set(f"Interpretation: {interp}")
            self.result_label.configure(foreground=color)

        except ValueError:
            messagebox.showerror("Input Error", "Please enter valid height and age.")

    def export_to_pdf(self):
        if not self.result_var.get():
            messagebox.showinfo("Export", "Please predict the liver span first.")
            return

        filename = filedialog.asksaveasfilename(
            defaultextension=".pdf",
            filetypes=[("PDF Files", "*.pdf")],
            title="Save Report As"
        )

        if filename:
            c = canvas.Canvas(filename)
            c.setFont("Helvetica-Bold", 14)
            c.drawString(100, 800, "Liver Span Prediction Report")

            c.setFont("Helvetica", 12)
            y = 760
            c.drawString(50, y, f"Name: {self.name_var.get()}")
            y -= 20
            c.drawString(50, y, f"Age: {self.age_var.get()} years")
            y -= 20
            c.drawString(50, y, f"Height: {self.height_var.get()} cm")
            y -= 40
            c.drawString(50, y, self.result_var.get())
            y -= 20
            c.drawString(50, y, self.interpretation_var.get())
            y -= 40
            c.drawString(50, y, "*Generated using Belema's Law of Hepatic-Height Correlation")

            c.save()
            messagebox.showinfo("Export", "PDF saved successfully.")

if __name__ == "__main__":
    root = tk.Tk()
    app = HepaticSpanApp(root)
    root.mainloop()