import { useState, useEffect } from 'react'
import { useSearchParams } from 'react-router-dom'
import api from '../utils/api'
import ProductGrid from '../components/product/ProductGrid'
import { PageLoader } from '../components/common/Spinner'

export default function ProductsPage() {
  const [products, setProducts] = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(1)
  const [totalPages, setTotalPages] = useState(1)
  const [total, setTotal] = useState(0)
  const [searchParams, setSearchParams] = useSearchParams()
  const [keyword, setKeyword] = useState(searchParams.get('keyword') || '')
  const selectedCategory = searchParams.get('category') || ''

  useEffect(() => {
    api.get('/products/categories').then(r => setCategories(r.data))
  }, [])

  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true)
      try {
        const params = new URLSearchParams()
        if (keyword) params.set('keyword', keyword)
        if (selectedCategory) params.set('category', selectedCategory)
        params.set('page', page)
        const { data } = await api.get(`/products?${params}`)
        setProducts(data.products)
        setTotalPages(data.pages)
        setTotal(data.total)
      } catch (err) {
        console.error(err)
      } finally {
        setLoading(false)
      }
    }
    fetchProducts()
  }, [keyword, selectedCategory, page])

  const handleSearch = (e) => {
    e.preventDefault()
    const val = e.target.search.value.trim()
    setKeyword(val)
    setPage(1)
    const params = new URLSearchParams(searchParams)
    if (val) params.set('keyword', val)
    else params.delete('keyword')
    setSearchParams(params)
  }

  const handleCategory = (cat) => {
    setPage(1)
    const params = new URLSearchParams()
    if (cat) params.set('category', cat)
    if (keyword) params.set('keyword', keyword)
    setSearchParams(params)
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Header */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">
          {selectedCategory || 'All Products'}
        </h1>
        {total > 0 && !loading && (
          <p className="text-gray-500 mt-1">{total} product{total !== 1 ? 's' : ''} found</p>
        )}
      </div>

      {/* Search */}
      <form onSubmit={handleSearch} className="flex gap-2 mb-6 max-w-lg">
        <input
          name="search"
          type="text"
          defaultValue={keyword}
          placeholder="Search products..."
          className="input-field"
        />
        <button type="submit" className="btn-primary px-5 shrink-0">Search</button>
      </form>

      <div className="flex flex-col md:flex-row gap-6">
        {/* Sidebar */}
        <aside className="md:w-52 shrink-0">
          <div className="bg-white rounded-xl shadow-sm p-4">
            <h3 className="font-semibold text-gray-800 mb-3">Categories</h3>
            <ul className="space-y-1">
              <li>
                <button
                  onClick={() => handleCategory('')}
                  className={`w-full text-left px-3 py-2 rounded-lg text-sm transition-colors ${!selectedCategory ? 'bg-blue-50 text-blue-700 font-semibold' : 'text-gray-700 hover:bg-gray-50'}`}
                >
                  All Products
                </button>
              </li>
              {categories.map((cat) => (
                <li key={cat}>
                  <button
                    onClick={() => handleCategory(cat)}
                    className={`w-full text-left px-3 py-2 rounded-lg text-sm transition-colors ${selectedCategory === cat ? 'bg-blue-50 text-blue-700 font-semibold' : 'text-gray-700 hover:bg-gray-50'}`}
                  >
                    {cat}
                  </button>
                </li>
              ))}
            </ul>
          </div>
        </aside>

        {/* Grid */}
        <div className="flex-1">
          {loading ? (
            <PageLoader />
          ) : (
            <>
              <ProductGrid products={products} />

              {/* Pagination */}
              {totalPages > 1 && (
                <div className="flex justify-center gap-2 mt-8">
                  <button
                    onClick={() => setPage(p => Math.max(1, p - 1))}
                    disabled={page === 1}
                    className="btn-secondary py-2 px-4 disabled:opacity-40"
                  >← Prev</button>
                  {Array.from({ length: totalPages }, (_, i) => i + 1).map((p) => (
                    <button
                      key={p}
                      onClick={() => setPage(p)}
                      className={`w-10 h-10 rounded-lg font-semibold text-sm transition-colors ${p === page ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 hover:bg-gray-100 border border-gray-200'}`}
                    >
                      {p}
                    </button>
                  ))}
                  <button
                    onClick={() => setPage(p => Math.min(totalPages, p + 1))}
                    disabled={page === totalPages}
                    className="btn-secondary py-2 px-4 disabled:opacity-40"
                  >Next →</button>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  )
}
