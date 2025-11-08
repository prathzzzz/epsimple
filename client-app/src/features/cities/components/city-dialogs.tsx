import { useQueryClient } from '@tanstack/react-query'
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog'
import { useCityContext } from '../context/city-provider'

export function CityDialogs() {
  const { 
    isBulkUploadDialogOpen,
    closeBulkUploadDialog,
  } = useCityContext()
  const queryClient = useQueryClient()

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ['cities'] })
  }

  const bulkUploadConfig = {
    entityName: 'City',
    uploadEndpoint: '/api/cities/bulk-upload',
    errorReportEndpoint: '/api/cities/export-errors',
    onSuccess: handleBulkUploadSuccess,
  }

  return (
    <>
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={bulkUploadConfig}
      />
    </>
  )
}
