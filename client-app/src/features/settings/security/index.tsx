import { ContentSection } from '../components/content-section'
import { ChangePasswordForm } from '../account/change-password-form'

export function SettingsSecurity() {
  return (
    <ContentSection
      title='Change Password'
      desc='Update your password to keep your account secure.'
    >
      <ChangePasswordForm />
    </ContentSection>
  )
}
