const currencyFormatter = new Intl.NumberFormat('es-EC', {
  style: 'currency',
  currency: 'USD',
});

export function formatCurrency(value) {
  return currencyFormatter.format(value);
}

export function formatDate(value) {
  if (!value) {
    return '';
  }
  const [year, month, day] = value.substring(0, 10).split('-');
  return `${day}/${month}/${year}`;
}