export const formatPrice = (price) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(price)

export const formatDate = (date) =>
  new Date(date).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })

export const getDiscountPercent = (original, current) =>
  original > current ? Math.round(((original - current) / original) * 100) : 0

export const truncate = (str, n) => str?.length > n ? str.substring(0, n) + '...' : str
