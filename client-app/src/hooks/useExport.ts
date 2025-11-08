import { useState } from 'react'
import { toast } from 'sonner'
import { downloadFile } from '@/lib/api-utils'

interface ExportConfig {
  entityName: string // "State", "City", etc.
  exportEndpoint: string // "/api/states/export"
}

export function useExport(config: ExportConfig) {
  const [isExporting, setIsExporting] = useState(false)

  const handleExport = async () => {
    setIsExporting(true)
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5)
      await downloadFile(config.exportEndpoint, `${config.entityName}s_Export_${timestamp}.xlsx`)
      toast.success(`${config.entityName}s exported successfully`)
    } catch (error) {
      toast.error(`Failed to export ${config.entityName}s`, {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
    } finally {
      setIsExporting(false)
    }
  }

  return {
    isExporting,
    handleExport,
  }
}
